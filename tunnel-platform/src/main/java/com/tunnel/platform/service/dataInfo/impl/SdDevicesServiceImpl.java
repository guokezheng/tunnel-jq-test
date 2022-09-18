package com.tunnel.platform.service.dataInfo.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.util.StringUtil;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.platform.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.platform.domain.dataInfo.SdFixedCode;
import com.tunnel.platform.domain.dataInfo.*;
import com.tunnel.platform.mapper.dataInfo.InductionlampControlStatusParamMapper;
import com.tunnel.platform.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.platform.service.dataInfo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备Service业务层处理
 *
 * @author zhangweitian
 * @date 2020-08-27
 */
@Service
public class SdDevicesServiceImpl implements ISdDevicesService
{
    private static final Logger log = LoggerFactory.getLogger(SdDevicesServiceImpl.class);
    @Autowired
    private SdDevicesMapper sdDevicesMapper;
    @Autowired
    private ISdDeviceCmdService sdDeviceCmdService;
    @Autowired
    private ISdEquipmentStateService sdEquipmentStateService;
    @Autowired
    private ISdFixedCodeService sdFixedCodeService;
    @Autowired
    private ISdTunnelsService sdTunnelsService;

    @Autowired
    private ISdEquipmentTypeService equipmentTypeService;

    @Autowired
    private IInductionlampControlStatusDetailsService iInductionlampControlStatusDetailsService;
    @Autowired
    private IInductionlampControlStatusParamService iInductionlampControlStatusParamService;
    @Autowired
    private InductionlampControlStatusParamMapper inductionlampControlStatusParamMapper;


    /**
     * 查询设备
     *
     * @param eqId 设备ID
     * @return 设备
     */
    @Override
    public SdDevices selectSdDevicesById(String eqId)
    {
        return sdDevicesMapper.selectSdDevicesById(eqId);
    }

    /**
     * 查询设备详情
     *
     * @param eqId 设备ID
     * @return 设备
     */
    @Override
    public Map<String,String> queryDeviceById(String eqId) {
        return sdDevicesMapper.queryDeviceById(eqId);
    }

    /**
     * 查询设备列表
     *
     * @param sdDevices 设备
     * @return 设备
     */
    @Override
    public List<SdDevices> selectSdDevicesList(SdDevices sdDevices)
    {
        List<SdDevices> devicesList = sdDevicesMapper.selectDropSdDevicesList(sdDevices);
        return devicesList;
    }

    /**
     * 新增设备
     *
     * @param sdDevices 设备
     * @return 结果
     */
    @Override
    public int insertSdDevices(SdDevices sdDevices)
    {
        sdDevices.setCreateTime(DateUtils.getNowDate());
        //判断当前是否是诱导灯设备
        Long yddEqTypeId = Long.parseLong(String.valueOf(DevicesTypeEnum.YOU_DAO_DENG.getCode()));
        if (null != sdDevices.getEqType() && sdDevices.getEqType().longValue() == yddEqTypeId.longValue()
                && (sdDevices.getControlStatus() == null || "".equals(sdDevices.getControlStatus()))) {
            String eqId = sdDevices.getEqId();
            //诱导灯设备暂时只对接手动控制模式，直接指定并默认运行模式为0
            sdDevices.setControlStatus("1");
            InductionlampControlStatusDetails inductionlampControlStatusDetails = new InductionlampControlStatusDetails();
            inductionlampControlStatusDetails.setEquipmentId(eqId);
            inductionlampControlStatusDetails.setEquipmentModeType(0);
            inductionlampControlStatusDetails.setBrightness("50");
            inductionlampControlStatusDetails.setFrequency("69");
            inductionlampControlStatusDetails.setCreateTime(DateUtils.getNowDate());
            iInductionlampControlStatusDetailsService.insertInductionlampControlStatusDetails(inductionlampControlStatusDetails);
            InductionlampControlStatusParam inductionlampControlStatusParam = new InductionlampControlStatusParam();
            inductionlampControlStatusParam.setEquipmentId(eqId);
            List<InductionlampControlStatusParam> controlStatusParams = iInductionlampControlStatusParamService.selectInductionlampControlStatusParamList(inductionlampControlStatusParam);
            if (controlStatusParams == null || controlStatusParams.size() == 0) {
                if (controlStatusParams.size() < 6) {
                    for (int i = 0;i < controlStatusParams.size();i++) {
                        iInductionlampControlStatusParamService.deleteInductionlampControlStatusParamById(controlStatusParams.get(i).getId());
                    }
                }
                //创建默认模式类型
                InductionlampControlStatusParam statusParam0 = new InductionlampControlStatusParam();
                statusParam0.setEquipmentId(eqId);
                statusParam0.setModeName("模式0");
                statusParam0.setModeCode(0);
                //温度
                statusParam0.setTemperatureStart(0);
                //湿度
                statusParam0.setHumidityEnd(89);
                //光照
                statusParam0.setIlluminationStart(1000);
                inductionlampControlStatusParamMapper.insertInductionlampControlStatusParam(statusParam0);

                InductionlampControlStatusParam statusParam1 = new InductionlampControlStatusParam();
                statusParam1.setEquipmentId(eqId);
                statusParam1.setModeName("模式1");
                statusParam1.setModeCode(1);
                //温度
                statusParam1.setTemperatureEnd(0);
                //湿度
                statusParam1.setHumidityStart(90);
                //光照
                statusParam1.setIlluminationEnd(1000);
                //亮度
                statusParam1.setBrightnessParam(50);
                inductionlampControlStatusParamMapper.insertInductionlampControlStatusParam(statusParam1);

                InductionlampControlStatusParam statusParam2 = new InductionlampControlStatusParam();
                statusParam2.setEquipmentId(eqId);
                statusParam2.setModeName("模式2");
                statusParam2.setModeCode(2);
                //光照
                statusParam2.setIlluminationEnd(500);
                //闪烁/每秒
                statusParam2.setTimeSecond(30);
                inductionlampControlStatusParamMapper.insertInductionlampControlStatusParam(statusParam2);
            }
        }
        return sdDevicesMapper.insertSdDevices(sdDevices);
    }

    /**
     * 修改设备
     *
     * @param sdDevices 设备
     * @return 结果
     */
    @Override
    public int updateSdDevices(SdDevices sdDevices)
    {
        sdDevices.setUpdateTime(DateUtils.getNowDate());
        String eqId = sdDevices.getEqId();
        Long yddEqTypeId = Long.parseLong(String.valueOf(DevicesTypeEnum.YOU_DAO_DENG.getCode()));
        if (null != sdDevices.getEqType() && sdDevices.getEqType().longValue() == yddEqTypeId.longValue()) {
            InductionlampControlStatusDetails inductionlampControlStatusDetails = new InductionlampControlStatusDetails();
            inductionlampControlStatusDetails.setEquipmentId(eqId);
            List<InductionlampControlStatusDetails> controlStatusDetails = iInductionlampControlStatusDetailsService.selectInductionlampControlStatusDetailsList(inductionlampControlStatusDetails);
            if (controlStatusDetails == null || controlStatusDetails.size() == 0) {
                inductionlampControlStatusDetails.setEquipmentModeType(0);
                inductionlampControlStatusDetails.setBrightness("50");
                inductionlampControlStatusDetails.setFrequency("69");
                iInductionlampControlStatusDetailsService.insertInductionlampControlStatusDetails(inductionlampControlStatusDetails);
            }
            sdDevices.setControlStatus("1");
            InductionlampControlStatusParam inductionlampControlStatusParam = new InductionlampControlStatusParam();
            inductionlampControlStatusParam.setEquipmentId(eqId);
            List<InductionlampControlStatusParam> controlStatusParams = iInductionlampControlStatusParamService.selectInductionlampControlStatusParamList(inductionlampControlStatusParam);
            if (controlStatusParams == null || controlStatusParams.size() == 0) {
                if (controlStatusParams.size() < 3) {
                    for (int i = 0;i < controlStatusParams.size();i++) {
                        iInductionlampControlStatusParamService.deleteInductionlampControlStatusParamById(controlStatusParams.get(i).getId());
                    }
                }
                //创建默认模式类型
                InductionlampControlStatusParam statusParam0 = new InductionlampControlStatusParam();
                statusParam0.setEquipmentId(eqId);
                statusParam0.setModeName("模式0");
                statusParam0.setModeCode(0);
                //温度
                statusParam0.setTemperatureStart(0);
                //湿度
                statusParam0.setHumidityEnd(89);
                //光照
                statusParam0.setIlluminationStart(1000);
                inductionlampControlStatusParamMapper.insertInductionlampControlStatusParam(statusParam0);

                InductionlampControlStatusParam statusParam1 = new InductionlampControlStatusParam();
                statusParam1.setEquipmentId(eqId);
                statusParam1.setModeName("模式1");
                statusParam1.setModeCode(1);
                //温度
                statusParam1.setTemperatureEnd(0);
                //湿度
                statusParam1.setHumidityStart(90);
                //光照
                statusParam1.setIlluminationEnd(1000);
                //亮度
                statusParam1.setBrightnessParam(50);
                inductionlampControlStatusParamMapper.insertInductionlampControlStatusParam(statusParam1);

                InductionlampControlStatusParam statusParam2 = new InductionlampControlStatusParam();
                statusParam2.setEquipmentId(eqId);
                statusParam2.setModeName("模式2");
                statusParam2.setModeCode(2);
                //光照
                statusParam2.setIlluminationEnd(500);
                //闪烁/每秒
                statusParam2.setTimeSecond(30);
                inductionlampControlStatusParamMapper.insertInductionlampControlStatusParam(statusParam2);
            }
        } else {
            InductionlampControlStatusDetails inductionlampControlStatusDetails = new InductionlampControlStatusDetails();
            inductionlampControlStatusDetails.setEquipmentId(eqId);
            List<InductionlampControlStatusDetails> controlStatusDetails = iInductionlampControlStatusDetailsService.selectInductionlampControlStatusDetailsList(inductionlampControlStatusDetails);
            if (controlStatusDetails.size() > 0) {
                for (int i = 0;i < controlStatusDetails.size();i++) {
                    Long id = controlStatusDetails.get(i).getId();
                    iInductionlampControlStatusDetailsService.deleteInductionlampControlStatusDetailsById(id);
                }
            }
            InductionlampControlStatusParam inductionlampControlStatusParam = new InductionlampControlStatusParam();
            inductionlampControlStatusParam.setEquipmentId(eqId);
            List<InductionlampControlStatusParam> controlStatusParams = iInductionlampControlStatusParamService.selectInductionlampControlStatusParamList(inductionlampControlStatusParam);
            if (controlStatusParams.size() > 0) {
                for (int i = 0; i < controlStatusParams.size(); i++) {
                    iInductionlampControlStatusParamService.deleteInductionlampControlStatusParamById(controlStatusParams.get(i).getId());
                }
            }
        }
        return sdDevicesMapper.updateSdDevices(sdDevices);
    }

    /**
     * 批量删除设备
     *
     * @param eqIds 需要删除的设备ID
     * @return 结果
     */
    @Override
    public int deleteSdDevicesByIds(String[] eqIds)
    {
        for (int j = 0;j < eqIds.length;j++) {
            String eqId = eqIds[j];
            InductionlampControlStatusDetails inductionlampControlStatusDetails = new InductionlampControlStatusDetails();
            inductionlampControlStatusDetails.setEquipmentId(eqId);
            List<InductionlampControlStatusDetails> controlStatusDetails = iInductionlampControlStatusDetailsService.selectInductionlampControlStatusDetailsList(inductionlampControlStatusDetails);
            if (controlStatusDetails.size() > 0) {
                for (int i = 0;i < controlStatusDetails.size();i++) {
                    Long id = controlStatusDetails.get(i).getId();
                    iInductionlampControlStatusDetailsService.deleteInductionlampControlStatusDetailsById(id);
                }
            }
            InductionlampControlStatusParam inductionlampControlStatusParam = new InductionlampControlStatusParam();
            inductionlampControlStatusParam.setEquipmentId(eqId);
            List<InductionlampControlStatusParam> controlStatusParams = iInductionlampControlStatusParamService.selectInductionlampControlStatusParamList(inductionlampControlStatusParam);
            if (controlStatusParams.size() > 0) {
                for (int i = 0; i < controlStatusParams.size(); i++) {
                    iInductionlampControlStatusParamService.deleteInductionlampControlStatusParamById(controlStatusParams.get(i).getId());
                }
            }
        }
        return sdDevicesMapper.deleteSdDevicesByIds(eqIds);
    }

    /**
     * 删除设备信息
     *
     * @param eqId 设备ID
     * @return 结果
     */
    @Override
    public int deleteSdDevicesById(String eqId)
    {
        return sdDevicesMapper.deleteSdDevicesById(eqId);
    }
    /**
     * 查询传感器信息
     */
    @Override
    public List<SdDevices> selectSensorList() {
        return sdDevicesMapper.selectSensorList();
    }
    /**
     * 根据隧道ID查询传感器信息
     */
    @Override
    public List<SdDevices> selectSensorListByTunnelId(String eqTunnelId,String direction) {
    	return sdDevicesMapper.selectSensorListByTunnelId(eqTunnelId,direction);
    }
    @Override
    public List<SdDevices> selectRippleListByTunnelId(String eqTunnelId,String direction) {
    	return sdDevicesMapper.selectRippleListByTunnelId(eqTunnelId,direction);
    }
    /**
     * 通过隧道id查询设备信息
     */
    @Override
    public List<Map<String,Object>> selectSdDevicesByTunnelId(SdDevices sdDevices) {
        List<Map<String,Object>> listMap= new ArrayList<>();
        List<SdDevices> list = sdDevicesMapper.selectSdDevicesByTunnelId(sdDevices);//查询所有的设备类型名称
        for (SdDevices devices : list) {
            Map<String, Object> map = new HashMap<>();
            int count = sdDevicesMapper.selectTypeNameCount(devices.getEqType(), devices.getEqTunnelId());//查询每种设备类型对应的设备数量
            map.put("count", count);
            map.put("eqTypeName", devices.getEqName());
            listMap.add(map);
        }

        return listMap;
    }
    /*@Override
    public List<SdDevices> selectSdDevicesEqidList(Long eqHostId) {
        return sdDevicesMapper.selectSdDevicesEqidList(eqHostId);
    }*/

    //根据设备类型查询相应的设备列表
    @Override
    public List<SdDevices> selectEqListByEqTypes(String[] eqTypes) {
        return sdDevicesMapper.selectEqListByEqTypes(eqTypes);
    }
    /**
     * 检索查询，控制指令出错的数据
     */
    @Override
    public List<SdDevices>  getChecklist(List<SdDevices> checklist){
        List<SdEquipmentState> list = sdEquipmentStateService.selectSdEquipmentStateList(new SdEquipmentState());
        List<SdDevices> devicesList= new ArrayList<>();
        for (SdDevices devices:checklist) {
            //查询指令 不为空
            if (StringUtils.isNotNull(devices.getEqControlPointAddress()) && StringUtils.isNotEmpty(devices.getEqControlPointAddress())){
                if(StringUtils.isNotNull(devices.getInstructionSeat()) && StringUtils.isNotEmpty(devices.getInstructionSeat())){
                    //校验DM模式+IP+机位
                    if (devices.getInstructionSeat().contains("DM")){

                        String[] dmeast = devices.getInstructionSeat().split("_");
                        StringBuffer dmQuery = getCommandCode(devices, dmeast[1],dmeast[0],"0");
                        //DM控制（模式+机位）不为空
                        if (StringUtils.isNotNull(devices.getDmcontrolSeat()) && StringUtils.isNotEmpty(devices.getDmcontrolSeat())){
                            String[] dmcontrolSeat = devices.getDmcontrolSeat().split("_");
                            StringBuffer dmControl = getCommandCode(devices, dmcontrolSeat[1], dmcontrolSeat[0],"1");
                            //DM控制命令或者DM查询命令 校验
                            if (!getControlCommands(devices,dmControl.toString(),list) || !devices.getEqControlPointAddress().contains(dmQuery.toString())){
                                devicesList.add(devices);
                            }
                        }else {
                            //DM查询 （模式+机位）不为空
                            if (!devices.getEqControlPointAddress().contains(dmQuery.toString())){
                                devicesList.add(devices);
                            }
                        }
                    }else {
                        //CIO
                        String[] cioeast = devices.getInstructionSeat().split("_");
                        //查询命令
                        StringBuffer cioQuery= getCommandCode(devices,cioeast[1],cioeast[0],"0");
                        // StringBuffer cioControl= cioControlAndQuery(plcIp,cioeast[1],"CIO_1");//控制
                        //|| !getControlCommands(devices).toString().contains(cioControl.toString())
                        if (!devices.getEqControlPointAddress().contains(cioQuery.toString()) ){
                            devicesList.add(devices);
                        }
                    }
                }else {
                    //只校验IP
//                    devicesList= checkIp(devices,devicesList,list);
                }

            }else {
                devicesList.add(devices);
            }
        }
        return devicesList;
    }

    public List<SdDevices> checkIp(SdDevices devices,List<SdDevices> devicesList,List<SdEquipmentState> list){
        String devicesIp = devices.getEqControlPointAddress().substring(40, 42);
        SdDevices plc = sdDevicesMapper.selectSdDevicesById(devices.getFEqId());
        String plcIp = plc.getIp().split("\\.")[3];
        if (!intToHex(Integer.parseInt(plcIp)).equals(devicesIp)){
            //查询指令出错
            devicesList.add(devices);
        }else {
            //控制指令
            SdDeviceCmd sdDeviceCmd=new SdDeviceCmd();
            sdDeviceCmd.setCodeDeviceId(devices.getEqId());
            List<SdDeviceCmd> cmdList = sdDeviceCmdService.selectSdDeviceCmdList(sdDeviceCmd);
            if (cmdList.size()>0){
                for (SdDeviceCmd sdDeviceCmd1:cmdList){
                    String command = sdDeviceCmd1.getCommand().substring(40, 42);
                    if (!intToHex(Integer.parseInt(plcIp)).equals(command)){
                        //查询指令出错
                        devicesList.add(devices);
                        break;
                    }
                }
            }else {
                if (!isControl(devices,list)){
                    devicesList.add(devices);
                }
            }
        }
        return devicesList;
    }
    //获取控制指令列表
    public Boolean getControlCommands(SdDevices devices,String dmControl,List<SdEquipmentState> list){
        //控制指令
        SdDeviceCmd sdDeviceCmd=new SdDeviceCmd();
        sdDeviceCmd.setCodeDeviceId(devices.getEqId());
        List<SdDeviceCmd> cmdList = sdDeviceCmdService.selectSdDeviceCmdList(sdDeviceCmd);
        if (cmdList.size()>0){
            for (SdDeviceCmd sdDeviceCmd1:cmdList){
                String command = sdDeviceCmd1.getCommand();
                if (!command.contains(dmControl)){
                    return false;
                }
            }
        }else {
            if (!isControl(devices,list)){
                return false;
            }
        }
        return true;
    }
    //DM 查询
    public StringBuffer getCommandCode(SdDevices devices,String seat,String deviceState,String deviceType){
        StringBuffer sb=new StringBuffer();
        SdFixedCode sdFixedCode = new SdFixedCode();
        sdFixedCode.setModelCode(deviceState);
        if ("0".equals(deviceType)){
            sdFixedCode.setQueryControl("0101");
        }else {
            sdFixedCode.setQueryControl("0102");
        }
        List<SdFixedCode> sdFixedCodes = sdFixedCodeService.selectSdFixedCodeList(sdFixedCode);
        if (sdFixedCodes.size()>0){
            sb.append(sdFixedCodes.get(0).getFixedCode());
            SdDevices plc = sdDevicesMapper.selectSdDevicesById(devices.getFEqId());
            String ip = plc.getIp().split("\\.")[3];
            sb.append(getIpleftPad(intToHex(Integer.parseInt(ip)))+"00");//目标ip地址
            sb.append(sdFixedCodes.get(0).getLocalIp());//本地IP
            sb.append("00"+sdFixedCodes.get(0).getQueryControl());//查询/控制
            sb.append(sdFixedCodes.get(0).getModelNum());//查询
            sb.append(getIpleftPad(intToHex(Integer.parseInt(seat)))+"00");//机位
            if (sdFixedCodes.get(0).getControlCode()!=null){
                sb.append(sdFixedCodes.get(0).getControlCode());//DM控制固定码
            }
        }
        return sb;
    }
    public  String intToHex(int n) {
        StringBuffer s = new StringBuffer();
        String a;
        char []b = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        while(n != 0){
            s = s.append(b[n%16]);
            n = n/16;
        }
        a = s.reverse().toString();
        return a;
    }
    public String getIpleftPad(String ip){
        return StringUtils.leftPad(ip, 4, "0");
    }
    //校验控制操作
    public Boolean isControl(SdDevices devices,List<SdEquipmentState> list){

        for (SdEquipmentState sdEquipmentState:list){
            if (sdEquipmentState.getStateTypeId().equals(devices.getEqType())){
                return false;//有权限
            }
        }
        return true;
    }
    @Override
    public String importSdDevices(List<SdDevices> sdDevicesList, Boolean isUpdateSupport, String operName){
        if (StringUtils.isNull(sdDevicesList) || sdDevicesList.size() == 0)
        {
            throw new ServiceException("导入设备数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SdDevices devices : sdDevicesList)
        {
            try
            {
                SdDevices d = sdDevicesMapper.selectSdDevicesById(devices.getEqId());
                if (StringUtils.isNull(d))
                {
                    Map map = checkDevices( devices);
                    if ((Boolean)map.get("flag")){
                        //todo 目前没有点位信息，先注释掉生成指令相关代码
//                        StringBuilder sb=new StringBuilder();
//                        sb.append(getCommandCode(devices,devices.getInstructionSeat().split("_")[1],devices.getInstructionSeat().split("_")[0],"0"));
//                        sb.append(getIpleftPad(devices.getEqControlPointAddress()));//点位地址
//                        devices.setEqControlPointAddress(sb.toString());
                        devices.setCreateBy(operName);
                        this.insertSdDevices(devices);
//                        insertOrUpdateOrDeleteSdDeviceCmd(devices);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、设备ID " + devices.getEqId() + " 导入成功");
                    }else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + map.get("failureMsg").toString());
                    }
                }
                else if (isUpdateSupport)
                {
                    Map map = checkDevices( devices);
                    if ((Boolean)map.get("flag")){
                        devices.setUpdateBy(operName);
                        //todo 目前没有点位信息，先注释掉生成指令相关代码
//                        StringBuilder sb=new StringBuilder();
//                        sb.append(getCommandCode(devices,devices.getInstructionSeat().split("_")[1],devices.getInstructionSeat().split("_")[0],"0"));
//                        sb.append(getIpleftPad(devices.getEqControlPointAddress()));//点位地址
//                        devices.setEqControlPointAddress(sb.toString());
                        this.updateSdDevices(devices);
//                        insertOrUpdateOrDeleteSdDeviceCmd(devices);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、设备ID " + devices.getEqId() + " 更新成功");
                    }else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + map.get("failureMsg").toString());
                    }
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、设备ID " + devices.getEqId() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、设备ID " + devices.getEqId() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            if(successNum>0){
                failureMsg.insert(0,successNum+" 条数据导入成功"+ "，" + failureNum + " 条数据格式不正确，导入失败！错误如下：");
            }else {
                failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            }
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
    public Map checkDevices(SdDevices devices){
        StringBuilder failureMsg=new StringBuilder();
        Map<String ,Object> map=new HashMap<String ,Object>();
        Long eqType = devices.getEqType();
        String fEqId = devices.getFEqId();
        //所属隧道
        SdTunnels sdTunnels = sdTunnelsService.selectSdTunnelsById(devices.getEqTunnelId());
        //设备类型
        SdEquipmentType sdEquipmentType = equipmentTypeService.selectSdEquipmentTypeById(eqType);
//        SdEquipmentState sdEquipmentState = sdEquipmentStateService.selectSdEquipmentStateById(devices.getEqType());
        if(!StringUtil.isEmpty(fEqId)){
            //有些设备不需要关联PLC，只有当录入PLC主机ID时，才校验PLC
            SdDevices plc = sdDevicesMapper.selectSdDevicesById(fEqId);
            if (StringUtils.isNull(plc))
            {
                failureMsg.append("、设备ID " + devices.getEqId() + " PLC主机ID不正确");
                map.put("flag",false);
                map.put("failureMsg",failureMsg);
                return map;
            }
        } else if (StringUtils.isNull(sdTunnels))
        {
            failureMsg.append("、设备ID " + devices.getEqId() + " 所属隧道ID不正确");
            map.put("flag",false);
            map.put("failureMsg",failureMsg);
            return map;
        }else if (StringUtils.isNull(sdEquipmentType))
        {
            failureMsg.append("、设备ID " + devices.getEqId() + " 设备类型ID不正确");
            map.put("flag",false);
            map.put("failureMsg",failureMsg);
            return map;
        }
        //设备方向
        //devices.getEqDirection();
        //控制模式+机位
        //devices.getDmcontrolSeat();
        //查询模式+机位 指令校验
        /*if (devices.getInstructionSeat()!=null){
            StringBuffer sbquery;
            if (devices.getInstructionSeat().contains("DM")){
                sbquery= getCommandCode(sdHosts.getPlcIp().split("\\.")[3],devices.getInstructionSeat().split("DM")[1],"DM","0");//查询
            }else {
                sbquery= getCommandCode(sdHosts.getPlcIp().split("\\.")[3],devices.getInstructionSeat().split("CIO")[1],"CIO","0");//查询
            }
            if (!devices.getEqControlPointAddress().contains(sbquery.toString()) ){
                failureMsg.append("、设备ID " + devices.getEqId() + " 指令不正确");
                map.put("flag",false);
                map.put("failureMsg",failureMsg);
                return map;
            }
        }*/
        map.put("flag",true);
        return map;
    }

    /**
     *
     * @param devices
     * @return
     */
    @Override
    public void insertOrUpdateOrDeleteSdDeviceCmd(SdDevices devices){
        SdEquipmentState sdEquipmentState=new SdEquipmentState();
        sdEquipmentState.setStateTypeId(devices.getEqType());
        sdEquipmentState.setIsControl(1);
        List<SdEquipmentState> sdEquipmentStates = sdEquipmentStateService.selectSdEquipmentStateList(sdEquipmentState);
        if (sdEquipmentStates.size()>0){
            for (SdEquipmentState sdEquipmentState1 :sdEquipmentStates){
                //根据类型查询类型状态表
                SdDeviceCmd sdDeviceCmd=new SdDeviceCmd();
                sdDeviceCmd.setCodeDeviceId(devices.getEqId());
                sdDeviceCmd.setCodePlcId(devices.getFEqId());
                if (StringUtils.isNotNull(devices.getDmcontrolSeat()) && StringUtils.isNotEmpty(devices.getDmcontrolSeat())){
                    sdDeviceCmd.setDeviceTypeId(devices.getEqType());
                    sdDeviceCmd.setCodeDeviceState(sdEquipmentState1.getDeviceState());
                    StringBuilder sb=new StringBuilder();
                    sb.append(getCommandCode(devices,devices.getDmcontrolSeat().split("_")[1],devices.getDmcontrolSeat().split("_")[0],"1"));
                    sb.append(getIpleftPad(sdEquipmentState1.getDeviceState()));//点位地址
                    List<SdDeviceCmd> sdDeviceCmds = sdDeviceCmdService.selectSdDeviceCmdList(sdDeviceCmd);
                    if (sdDeviceCmds.size()>0){
                        sdDeviceCmds.get(0).setCommand(sb.toString());
                        sdDeviceCmdService.updateSdDeviceCmd(sdDeviceCmds.get(0));
                    }else {
                        sdDeviceCmd.setCommand(sb.toString());
                        sdDeviceCmdService.insertSdDeviceCmd(sdDeviceCmd);
                    }
                }/*else {
                        List<SdDeviceCmd> sdDeviceCmds = sdDeviceCmdService.selectSdDeviceCmdList(sdDeviceCmd);
                        for (SdDeviceCmd sdDeviceCmd1:sdDeviceCmds){
                            sdDeviceCmdService.deleteSdDeviceCmdById(sdDeviceCmd1.getCodeId());
                        }
                    }*/
            }
        }else {
            //没有设备状态，删除该设备关联的设备报文指令
            SdDeviceCmd sdDeviceCmd=new SdDeviceCmd();
            sdDeviceCmd.setCodeDeviceId(devices.getEqId());
            sdDeviceCmd.setCodePlcId(devices.getFEqId());
            List<SdDeviceCmd> sdDeviceCmds = sdDeviceCmdService.selectSdDeviceCmdList(sdDeviceCmd);
            for (SdDeviceCmd sdDeviceCmd1:sdDeviceCmds){
                sdDeviceCmdService.deleteSdDeviceCmdById(sdDeviceCmd1.getCodeId());
            }
        }

    }

    @Override
    public List<SdDevices> getAllPressureGaugesMsg() {
        Long deptId = SecurityUtils.getDeptId();
        return sdDevicesMapper.getAllPressureGaugesMsg(deptId);
    }


    @Override
    public List<Map<String, Object>> getDevicesStatus(String tunnelId) {
        List<Map<String, Object>> devicesStatus = sdDevicesMapper.getDevicesStatus(tunnelId);
//        List<Map<String, Object>> devicesType = sdDevicesMapper.getDevicesType(tunnelId);
//        devicesType.add(devicesStatus.get(0));
        return devicesStatus;
    }

    @Override
    public List<Map<String, Object>> obtainEquipmentEnergyConsumption(String tunnelId) {
        List<Map<String, Object>> devicesType = sdDevicesMapper.getDevicesType(tunnelId);
        for (int i = 0;i < devicesType.size();i++) {
            Map<String, Object> map = devicesType.get(i);
            map.put("electricityConsumption",String.valueOf(new Random().nextInt(2000)));
        }
        return devicesType;
    }

    @Override
    public String autoId(String tunnelId,Integer typeId) {
        if (typeId==1){
            return null;
        }
        //所有隧道类型
        List<SdEquipmentType> equipmentTypes=sdDevicesMapper.selectList();
        //当前设备类型的设备id 如果为null则该设备没添加
        Map<Long, String> collect=new HashMap<>();
        equipmentTypes.forEach(f->{
            collect.put(f.getTypeId(),f.getTypeAbbr());
        });
        //设备类型名称简称
        String typeAbbr = collect.get(typeId.longValue());
        String ss = tunnelId + "-" + typeAbbr;
        List<String> list = sdDevicesMapper.selectID(ss);
        log.info("当前设备类型名称简称-->>>>>>>>>>>>>>>>>>{}",typeAbbr);
        StringBuilder sb=new StringBuilder();
        if (CollectionUtils.isNotEmpty(list)){
            log.info("进来了--->当前设备类型数据库已有");
            List<Integer> integerList=list.stream().map(
                f->{
                    int i = f.lastIndexOf("-");
                    int length = f.length();
                    String s = f.substring(i+1, length);
                    return Integer.parseInt(s);
                }
            ).sorted().collect(Collectors.toList());
            Integer integer = integerList.get(integerList.size() - 1);
            String s = String.format("%0" + 3 + "d", integer + 1);
            sb.append(tunnelId).append("-").append(typeAbbr).append("-").append(s);
        }else {
            //当前设备类型在数据库新建
            log.info("----当前设备类型数据库新建----");
            sb.append(tunnelId).append("-").append(typeAbbr).append("-").append("001");
        }
        return sb.toString();
    }

    @Override
    public List<SdDevices> updateCarFingerById(List<SdDevices> sdDevices) {
        List<SdDevices> sdDevicesList = new ArrayList<>();
        for (SdDevices devices : sdDevices) {
            List<SdDevices> sdDevicesLists = sdDevicesMapper.selectCarFingerById(devices);
            sdDevicesLists.forEach( x -> sdDevicesList.add(x));
        }
        return sdDevicesList;
    }

    /**
     * 查询可控设备列表
     * @param sdDevices
     * @return
     */
    @Override
    public List<SdDevices> selectIsControlSdDevicesList(SdDevices sdDevices) {
        List<SdDevices> devicesList = sdDevicesMapper.selectIsControlSdDevicesList(sdDevices);
        return devicesList;
    }

    @Override
    public List<Map<String, Object>> getDevicesByTypeAndTunnel(SdDevices sdDevices) {
        List<Map<String, Object>> devicesByTypeAndTunnel = sdDevicesMapper.getDevicesByTypeAndTunnel(sdDevices);
        return devicesByTypeAndTunnel;
    }

    @Override
    public int updateSdDevicesByFEqId(SdDevices sdDevices) {
        sdDevices.setEqStatusTime(new Date());
        return sdDevicesMapper.updateSdDevicesByFEqId(sdDevices);
    }

}
