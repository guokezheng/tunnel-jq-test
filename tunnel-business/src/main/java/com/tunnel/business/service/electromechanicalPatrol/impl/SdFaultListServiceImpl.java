package com.tunnel.business.service.electromechanicalPatrol.impl;


import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdEquipmentStateIconFile;
import com.tunnel.business.domain.electromechanicalPatrol.SdFaultList;
import com.tunnel.business.domain.electromechanicalPatrol.SdPatrolList;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficImage;
import com.tunnel.business.mapper.electromechanicalPatrol.SdFaultListMapper;
import com.tunnel.business.mapper.electromechanicalPatrol.SdPatrolListMapper;
import com.tunnel.business.mapper.trafficOperationControl.eventManage.SdTrafficImageMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.electromechanicalPatrol.ISdFaultListService;
import com.tunnel.business.utils.util.UUIDUtil;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * 故障清单Service业务层处理
 *
 * @author ruoyi
 * @date 2022-11-02
 */
@Service
public class SdFaultListServiceImpl implements ISdFaultListService
{
    @Autowired
    private SdFaultListMapper sdFaultListMapper;

    @Autowired
    private SdTrafficImageMapper sdTrafficImageMapper;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private SdPatrolListMapper sdPatrolListService;


    /**
     * 查询故障清单
     *
     * @param id 故障清单主键
     * @return 故障清单
     */
    @Override
    public SdFaultList selectSdFaultListById(String id)
    {
        SdFaultList sdFaultList = sdFaultListMapper.selectSdFaultListById(id);
        if(sdFaultList!=null){
            if(sdFaultList.getImgFileId()!=null&&!"".equals(sdFaultList.getImgFileId())){
                SdTrafficImage sdTrafficImage = new SdTrafficImage();
                sdTrafficImage.setBusinessId(sdFaultList.getImgFileId());
                sdFaultList.setiFileList(sdTrafficImageMapper.selectFaultImgFileList(sdTrafficImage));
            }
        }
        return sdFaultList;
    }


    /**
     * 查询故障清单列表
     *
     * @param sdFaultList 故障清单
     * @return 故障清单
     */
    @Override
    public List<SdFaultList> selectSdFaultListList(SdFaultList sdFaultList)
    {
        List<SdFaultList> list =  sdFaultListMapper.selectSdFaultListList(sdFaultList);
        if(list!=null&&list.size()>0){
            for(int i = 0;i<list.size();i++){
                if(list.get(i).getFalltRemoveStatue()!=null&&!"null".equals(list.get(i).getFalltRemoveStatue())&&!"".equals(list.get(i).getFalltRemoveStatue())){
                    int removeflag =  Integer.valueOf(list.get(i).getFalltRemoveStatue());
                    if("1".equals(removeflag)){//未消除  根据当前时间与故障发现时间计算时间差，单位：天、小时
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if(list.get(i).getFaultFxtime()!=null){
                        String  fxtime = sdf.format(list.get(i).getFaultFxtime());
                        long time = sdf.parse(fxtime, new ParsePosition(0)).getTime();
                        System.out.println("++==================++=time===========+"+time);
                        long nd = 1000 * 24 * 60 * 60;
                        long nh = 1000 * 60 * 60;
                        long nm = 1000 * 60;
                        long ns = 1000;
                        // 获得两个时间的毫秒时间差异
                        long diff = System.currentTimeMillis() - time + 1000;
                        // 计算差多少天
                        long day = diff / nd;
                        // 计算差多少小时
                        long hour = diff % nd / nh;
                        // 计算差多少分钟
                        long min = diff % nd % nh / nm;
                        // 计算差多少秒//输出结果
                        long sec = diff % nd % nh % nm / ns;
                        System.out.println(day + "天" + hour + "小时" + min + "分钟" + sec + "秒");
                        list.get(i).setFaultCxtime(day + "天" + hour + "小时");
                    }
                    }
                }

            }

        }
        return list;
    }

    /**
     * 新增故障清单
     *
     * @param sdFaultList 故障清单
     * @return 结果
     */
    @Override
    public int insertSdFaultList(MultipartFile[] file, SdFaultList sdFaultList)
    {
        System.out.println("+++++++++=+++++++++++++++++++====="+file);
        int result = -1;
        List<SdTrafficImage> list = new ArrayList<SdTrafficImage>();
        try {
            sdFaultList.setFaultTbtime(DateUtils.getNowDate());//故障填报时间
            sdFaultList.setCreateTime(DateUtils.getNowDate());// 创建时间
            sdFaultList.setCreateBy(SecurityUtils.getUsername());// 设置当前创建人
            sdFaultList.setFaultTbr(SecurityUtils.getUsername());// 设置当前创建人
            if (sdFaultList.getEqId() != null && !sdFaultList.getEqId().equals("")
                    && (sdFaultList.getFaultLocation() == null || sdFaultList.getFaultLocation().equals(""))) {
                SdDevices sdDevices = sdDevicesService.selectSdDevicesById(sdFaultList.getEqId());
                sdFaultList.setFaultLocation(sdDevices.getPile());
            }
            if(file!=null){
                String guid = UUIDUtil.getRandom32BeginTimePK();// 生成guid
                sdFaultList.setImgFileId(guid);// 文件关联ID
                for (int i = 0; i < file.length; i++) {
                    // 图片Base64
                    String imageBaseStr = null;
                    try {
                        String contentType = file[i].getContentType();
                        if (!contentType.contains("image")) {
                            throw new RuntimeException("文件类型不正确!");
                        }
                        byte[] imageBytes = file[i].getBytes();
                        BASE64Encoder base64Encoder = new BASE64Encoder();
                        imageBaseStr = "data:" + contentType + ";base64," + base64Encoder.encode(imageBytes);
                        imageBaseStr = imageBaseStr.replaceAll("[\\s*\t\n\r]", "");
                    } catch (IOException e) {
                        throw new RuntimeException("图片转换base64异常");
                    }
                    // 从缓存中获取文件存储路径
                    String fileServerPath = RuoYiConfig.getUploadPath();
                    // 原图文件名
                    String filename = file[i].getOriginalFilename();
                    // 原图扩展名
                    String extendName = filename.substring(filename.lastIndexOf("\\") + 1);
                    // 新的全名
                    String fileName = extendName;
                    // 加路径全名
                    File dir = new File(fileServerPath + "/faultIcon/" + fileName);
                    File filepath = new File(fileServerPath + "/faultIcon");

                    SdTrafficImage iconFile = new SdTrafficImage();
                    iconFile.setBusinessId(guid);
                    iconFile.setImgUrl(imageBaseStr);
                    iconFile.setImgName(fileName);
                    iconFile.setCreateBy(SecurityUtils.getUsername());
                    iconFile.setCreateTime(DateUtils.getNowDate());
                    list.add(iconFile);

                    if (!filepath.exists()) {
                        filepath.mkdirs();
                    } else {
                    }
                    file[i].transferTo(dir);
                }
                result = sdTrafficImageMapper.brachInsertFaultIconFile(list);
                if (result > -1) {
                    result = sdFaultListMapper.insertSdFaultList(sdFaultList);
                }
            }else {
                sdFaultList.setImgFileId(null);// 图标文件ID
                result = sdFaultListMapper.insertSdFaultList(sdFaultList);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return result;
    }

    /**
     * 修改故障清单
     *
     * @param sdFaultList 故障清单
     * @return 结果
     */
    @Override
    public int updateSdFaultList(MultipartFile[] file,SdFaultList sdFaultList,String[] removeIds)
    {
        int result = 0;
        List<SdTrafficImage> list = new ArrayList<SdTrafficImage>();
        try {
            sdFaultList.setUpdateTime(DateUtils.getNowDate());// 创建时间
            sdFaultList.setUpdateBy(SecurityUtils.getUsername());// 设置当前创建人
            if (file != null) {
                String guid = sdFaultList.getImgFileId();// 关联ID--guid
                if (guid == null || guid.equals("null") || guid.equals("")) {
                    guid = UUIDUtil.getRandom32BeginTimePK();// 生成guid
                }
                sdFaultList.setImgFileId(guid);// 文件关联ID
                for (int i = 0; i < file.length; i++) {
                    // 图片Base64
                    String imageBaseStr = null;
                    try {
                        String contentType = file[i].getContentType();
                        if (!contentType.contains("image")) {
                            throw new RuntimeException("文件类型不正确!");
                        }
                        byte[] imageBytes = file[i].getBytes();
                        BASE64Encoder base64Encoder = new BASE64Encoder();
                        imageBaseStr = "data:" + contentType + ";base64," + base64Encoder.encode(imageBytes);
                        imageBaseStr = imageBaseStr.replaceAll("[\\s*\t\n\r]", "");
                    } catch (IOException e) {
                        throw new RuntimeException("图片转换base64异常");
                    }
                    // 从缓存中获取文件存储路径
                    String fileServerPath = RuoYiConfig.getUploadPath();
                    // 原图文件名
                    String filename = file[i].getOriginalFilename();
                    // 原图扩展名
                    String extendName = filename.substring(filename.lastIndexOf("\\") + 1);
                    // 新的全名
                    String fileName = extendName;
                    // 加路径全名
                    File dir = new File(fileServerPath + "/faultIcon/" + fileName);
                    File filepath = new File(fileServerPath + "/faultIcon");

                    SdTrafficImage iconFile = new SdTrafficImage();
                    iconFile.setBusinessId(guid);
                    iconFile.setImgUrl(imageBaseStr);
                    iconFile.setImgName(fileName);
                    iconFile.setCreateBy(SecurityUtils.getUsername());
                    iconFile.setCreateTime(DateUtils.getNowDate());
                    list.add(iconFile);

                    if (!filepath.exists()) {
                        filepath.mkdirs();
                    } else {
                    }
                    file[i].transferTo(dir);
                }
                result = sdTrafficImageMapper.brachInsertFaultIconFile(list);
            }
            if (removeIds.length > 0) {
                result = sdTrafficImageMapper.deleteFaultIconFileByIds(removeIds);//ids 为要删除的sd_traffic_image img_id数组
            }
            if (result >= 0) {
                result = sdFaultListMapper.updateSdFaultList(sdFaultList);
            }


        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }


        return result;

        //return sdFaultListMapper.updateSdFaultList(sdFaultList);
    }

    /**
     * 批量删除故障清单
     *
     * @param ids 需要删除的故障清单主键
     * @return 结果
     */
    @Override
    public int deleteSdFaultListByIds(String[] ids)
    {
        return sdFaultListMapper.deleteSdFaultListByIds(ids);
    }

    /**
     * 删除故障清单信息
     *
     * @param id 故障清单主键
     * @return 结果
     */
    @Override
    public int deleteSdFaultListById(String id)
    {
        return sdFaultListMapper.deleteSdFaultListById(id);
    }

    /**
     * 查询故障检修记录
     * @param faultId
     * @return
     */
    @Override
    public List<SdPatrolList> getFaultRepairInfo(String faultId) {
        List<SdPatrolList> patrolList = new ArrayList<>();
        //先判断是巡检点还是故障点
        SdPatrolList sdPatrolList = sdPatrolListService.faultOrDevices(faultId);
        //故障点
        if(sdPatrolList!=null&&"1".equals(sdPatrolList.getPatrolType())){
            patrolList = sdFaultListMapper.getFaultRepairInfo(faultId);

        }
        //巡检点
        if(sdPatrolList!=null&&"0".equals(sdPatrolList.getPatrolType())){
            patrolList = sdFaultListMapper.getDevicesRepairInfo(faultId);
        }
        if(patrolList!=null&&patrolList.size()>0){
            String fileId = patrolList.get(0).getImgFileId();
            if (fileId != null && !"".equals(fileId) && !"null".equals(fileId)) {
                SdTrafficImage sdTrafficImage = new SdTrafficImage();
                sdTrafficImage.setBusinessId(fileId);
                patrolList.get(0).setiFileList(sdTrafficImageMapper.selectFaultImgFileList(sdTrafficImage));
            }
        }



        return patrolList;
    }

    /**
     * 根据隧道、故障类型获取故障列表
     * @param tunnelId
     * @param faultLevel
     * @return
     */
    @Override
    public List<SdFaultList> getFaultList(String tunnelId, String faultLevel) {
        return sdFaultListMapper.getFaultList1(tunnelId,faultLevel);
    }

    @Override
    public String selectSdFaultEqById(String eqFaultId) {
        return sdFaultListMapper.selectSdFaultEqById(eqFaultId);
    }
}
